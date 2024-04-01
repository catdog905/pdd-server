package ru.catdog905
package controller

import cats.Functor
import cats.data.ReaderT
import cats.effect.IO
import sttp.tapir.server.ServerEndpoint
import cats.implicits.toFunctorOps

trait TodoController[F[_]] {
  def listAllTodos: ServerEndpoint[Any, F]
  def findTodoById: ServerEndpoint[Any, F]
  def removeTodoById: ServerEndpoint[Any, F]
  def createTodo: ServerEndpoint[Any, F]

  def all: List[ServerEndpoint[Any, F]]
}

//object TodoController {
//  final private class Impl(/*storage: TodoStorage[ReaderT[IO, RequestContext, *]]*/) extends TodoController[IO] {
//
//    override val listAllTodos: ServerEndpoint[Any, IO] =
//      endpoints.listTodos.serverLogic(ctx =>
//        storage.list.map(_.left.map[AppError](identity)).run(ctx)
//        //          storage.list.leftMapIn(identity[AppError])
//      )
//
//    override val all: List[ServerEndpoint[Any, IO]] =
//      List(listAllTodos, findTodoById, removeTodoById, createTodo)
//  }
//
//  def make(storage: TodoStorage[ReaderT[IO, RequestContext, *]]): TodoController[IO] = new Impl(storage)
//}