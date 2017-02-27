package domain
package service

import repository._
import cats.data.Kleisli

import scala.concurrent.Future

trait ArticleService[Article, URL, Publisher] {

  type ArticleOp[A] = Kleisli[Future, ArticleRepository, A]

  def query(from: Int = 0, size: Int = 10): ArticleOp[Seq[Article]]
  def store(article: Article): ArticleOp[Article]
  def store(seq: Seq[Article]): ArticleOp[Seq[Article]]

  def extract(terms: Seq[String], lang: String): Kleisli[Future, ExtractorRepository, Seq[Article]]
  def extract(url: URL, publisher: Publisher): Kleisli[Future, ExtractorRepository, Seq[Article]]
  def mainContent(article: Article): Kleisli[Future, ExtractorRepository, Article]

}
