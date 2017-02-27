package domain
package repository


import model._
import scala.concurrent.Future

trait ExtractorRepository {

  def extractRSS(url: String): Future[Seq[Feed]]
  def extract(terms: Seq[String], lang: String): Future[Seq[Article]]
  def extract(url: String, publisher: String): Future[Seq[Article]]
  def mainContent(article: Article): Future[Article]

}
