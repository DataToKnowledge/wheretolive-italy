package domain
package service

import repository._
import cats.data._

import scala.concurrent.Future

trait FeedService[Feed, URL] {

  type FeedOp[A] = Kleisli[Future, FeedRepository, A]

  def query(from: Int = 0, size: Int = 10): FeedOp[Seq[Feed]]
  def store(feed: Feed): FeedOp[Feed]
  def store(seq: Seq[Feed]): FeedOp[Seq[Feed]]

  def extractRSS(url: URL): Kleisli[Future, ExtractorRepository, Seq[Feed]]

}
