package fpinscala.datastructures

sealed trait List[+A] // `List` data type, parameterized on a type, `A`

case object Nil extends List[Nothing] // A `List` data constructor representing the empty list

/* Another data constructor, representing nonempty lists. Note that `tail` is another `List[A]`,
which may be `Nil` or another `Cons`.
 */
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List { // `List` companion object. Contains functions for creating and working with lists.
  def sum(ints: List[Int]): Int = ints match { // A function that uses pattern matching to add up a list of integers
    case Nil => 0 // The sum of the empty list is 0.
    case Cons(x,xs) => x + sum(xs) // The sum of a list starting with `x` is `x` plus the sum of the rest of the list.
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x,xs) => x * product(xs)
  }

  def apply[A](as: A*): List[A] = // Variadic function syntax
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  val x = List(1,2,3,4,5) match {
    case Cons(x, Cons(2, Cons(4, _))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t) => h + sum(t)
    case _ => 101
  }

  def append[A](a1: List[A], a2: List[A]): List[A] =
    a1 match {
      case Nil => a2
      case Cons(h,t) => Cons(h, append(t, a2))
    }

  def foldRight2[A, B](as: List[A], z: B)(f: (A, B) => B): B = // Utility functions
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight2(xs, z)(f))
    }

  def sum2(ns: List[Int]) =
    foldRight(ns, 0)((x,y) => x + y)

  def product2(ns: List[Double]) =
    foldRight(ns, 1.0)(_ * _) // `_ * _` is more concise notation for `(x,y) => x * y`; see sidebar


  def tail[A](l: List[A]): List[A] = l match {
    case Cons(l, Nil) => Nil //     case Nil => sys.error("tail of empty list")
    case Cons(h, t) => t //     case Cons(_,t) => t
    //
  }


  def setHead[A](l: List[A], h: A): List[A] =
  //    Cons(h,tail(l))
    l match {
      case Cons(_, t) => Cons(h, t)
      // test commit
    }

  def drop[A](l: List[A], n: Int): List[A] = {
    if (n <= 0) l
    else l match {
      case Nil => Nil
      case Cons(_, t) => drop(t, n - 1)
    }
  }

  def dropWhile[A](l: List[A], f: A => Boolean): List[A] =
    l match {
      case Cons(h, t) if f(h) => dropWhile(t, f)
      case _ => l
    }

  def init[A](l: List[A]): List[A] =
    l match {
      case Cons(_, Nil) => Nil
      case Cons(h, t) => Cons(h, init(t))
    }

  def length[A](l: List[A]): Int =
    foldRight(l, 0)((_, acc) => acc + 1)

  def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = // Utility functions
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def append2[A](l: List[A], r: List[A]): List[A] =
    foldRight(l, r)(Cons(_, _))

  @annotation.tailrec
  def foldLeft[A, B](l: List[A], z: B)(f: (B, A) => B): B =
    l match {
      case Nil => z
      case Cons(h, t) => foldLeft(t, f(z, h))(f)
    }

  def sum3(ns: List[Int]) =
    foldLeft(ns, 0)(_ + _)

  def product3(ns: List[Double]) =
    foldLeft(ns, 1.0)(_ * _)

  def length3[A](l: List[A]): Int =
    foldRight(l, 0)((_, acc) => acc + 1)

  def reverse[A](l: List[A]): List[A] =
  //    foldLeft(l, Nil)(Cons(A,l))
    foldLeft(l, Nil: List[A])((acc, h) => Cons(h, acc))
  //  foldLeft(l, List[A]())((acc, h) => Cons(h, acc))

  def concat[A](l: List[List[A]]): List[A] = {
    // List of lists into a single list. Runtime should be linear in the total length of all lists.
    // foldRight(l, Nil:List[A])(append) // Solution
    foldRight(l, Nil: List[A])((x, y) => append2(x, y))
  }

  def addOne(l: List[Int]): List[Int] = {
    foldRight(l, Nil: List[Int])((x, y) => Cons(x + 1, y))
  }

  def doubleListToString(l: List[Double]): List[String] = {
    foldRight(l, Nil: List[String])((x, y) => Cons(x.toString, y))
  }

  def map[A, B](l: List[A])(f: A => B): List[B] =
    foldRight(l, Nil: List[B])((h, t) => Cons(f(h), t))

  def filter[A](l: List[A])(f: A => Boolean): List[A] =
    foldRight(l, Nil: List[A])((h, t) => if (f(h)) Cons(h, t) else t)

  // * Solution
  def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] =
    concat(map(as)(f))

  def main(args: Array[String]): Unit = {
    //    println(tail(List(1, 2, 3, 4)))
    //    println(setHead(List(1, 2, 3, 4), 5))
    //    println(init(List(1, 2, 3, 4)))
    //    println(foldRight(List(1, 2, 3), Nil: List[Int])(Cons(_, _)))
    //    println(foldRight(List(1, 2, 3), List(8, 9, 10))(Cons(_, _)))
    //    println(sum3(List(1, 2, 3, 4)))
    //    println(product3(List(1, 2, 3, 4)))
    //    println(length3(List(1, 2, 3, 4)))
    //    println(reverse(List(1, 2, 3)))
    //    println(append2(List(1, 2), List(10, 11)))
    //    println(concat(List(List(1, 2), List(3, 4))))
    //    println(addOne(List(1, 2, 3)))
    //    println(doubleListToString(List(1.1, 2.3, 4.5)))
    //    println(filter(List(1, 2, 3, 4, 5, 6))(x => x % 2 != 0))
    //    println(flatMap(List(1, 2, 3))(i => List(i, i))) // should result in List(1,1,2,2,3,3)
  }

}
