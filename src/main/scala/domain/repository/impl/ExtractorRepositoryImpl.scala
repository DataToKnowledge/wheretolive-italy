package domain
package repository

import com.typesafe.scalalogging.Logger
import model.{Article, Feed}
import org.jsoup.Jsoup

import scala.concurrent.{ExecutionContext, Future}
import scalaj.http.Http
import scala.collection.JavaConverters._

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

    elements.map{ e =>
      Feed(
        url = e.attr("abs:href"),
        publisher = title,
        lang = lang
      )
    }
  }

  override def extract(terms: Seq[String], lang: String)(implicit ec: ExecutionContext) = ???

  override def extract(url: String, publisher: String)(implicit ec: ExecutionContext) = ???

  override def mainContent(article: Article)(implicit ec: ExecutionContext) = ???
}
