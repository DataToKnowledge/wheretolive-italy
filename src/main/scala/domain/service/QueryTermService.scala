package domain
package service

import repository.QueryTermRepository

import cats.data.Kleisli

import scala.concurrent.Future

trait QueryTermService[QueryTerm] {

  type QueryTermOp[A] = Kleisli[Future,QueryTermRepository, A]

  def query(from: Int, to: Int): QueryTermOp[Seq[QueryTerm]]
  def store(queryTerm: QueryTerm): QueryTermOp[QueryTerm]
  def store(seq: Seq[QueryTerm]): QueryTermOp[Seq[QueryTerm]]

}
