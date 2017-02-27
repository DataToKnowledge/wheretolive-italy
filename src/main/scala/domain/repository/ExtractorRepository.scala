package domain
package repository

import model._

import scala.concurrent.{ExecutionContext, Future}

trait ExtractorRepository {

  def extractRSS(url: String, lang: String)(implicit ec: ExecutionContext): Future[Seq[Feed]]
  def extract(terms: Seq[String], lang: String)(implicit ec: ExecutionContext): Future[Seq[Article]]
  def extract(url: String, publisher: String)(implicit ec: ExecutionContext): Future[Seq[Article]]
  def mainContent(article: Article)(implicit ec: ExecutionContext): Future[Article]

}
