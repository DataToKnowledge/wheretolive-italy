package domain
package repository

import java.net.URL
import java.time.{LocalDate, LocalDateTime}

import model.{Article, Feed}
import org.jsoup.Jsoup
import com.rometools.rome.io.{SyndFeedInput, XmlReader}
import com.typesafe.scalalogging.Logger

import scala.concurrent.{ExecutionContext, Future}
import scalaj.http.Http
import scala.collection.JavaConverters._
import scala.util.Try

trait ExtractorRepositoryImpl extends ExtractorRepository {

  private val logger = Logger("ExtractorRepository")

  override def extractRSS(url: String, lang: String)(implicit ec: ExecutionContext) = Future {
    logger.debug(s"extracting $url")
    val response = Http(url).asString
    logger.debug(s"extracted $url with code ${response.code}")

    val doc = Jsoup.parse(response.body, url)
    val title = doc.title()

    val elements = doc
      .select("link[type]").asScala
      .filter(_.attr("type") == "application/rss+xml")

    elements.map { e =>
      Feed(
        url = e.attr("abs:href"),
        publisher = title,
        lang = lang
      )
    }
  }

  override def extract(terms: Seq[String], lang: String)(implicit ec: ExecutionContext) = {
    val url = s"https://news.google.it/news?cf=all&hl=it&pz=1&ned=$lang&output=rss&q=${terms.mkString("+")}"
    extract(url, "Google News")
  }

  override def extract(url: String, publisher: String)(implicit ec: ExecutionContext) = Future {
    def extractText(html: String): String = Jsoup.parse(html).text()

    def urlKeywords(url: String): List[String] = {
      Try {
        val uri = new URL(url)
        uri.getPath.split("/|-|_").filter(_.length > 0).filterNot(_.contains(".")).toList
      } getOrElse List.empty[String]
    }

    val input = new SyndFeedInput()
    val reader = input.build(new XmlReader(new URL(url)))

    reader.getEntries.asScala.map { e =>

      val uri: String = {
        val index = if (e.getUri.lastIndexOf("http://") != -1) e.getUri.lastIndexOf("http://")
        else e.getUri.lastIndexOf("https://")

        e.getUri.substring(index, e.getUri.length)
      }

      val keywords = urlKeywords(uri).filter(_.length > 2)

      val article = Article(
        uri = uri,
        title = extractText(e.getTitle),
        description = extractText(e.getDescription.getValue),
        publisher = publisher,
        keywords = keywords,
        categories = e.getCategories.asScala.map(_.getName),
        imageUrl = e.getEnclosures.asScala.map(_.getUrl).mkString(""),
        date = e.getPublishedDate.toInstant.toEpochMilli
      )

      article.withText(article.description)
    }
  }

  override def mainContent(article: Article)(implicit ec: ExecutionContext) = ???
}
