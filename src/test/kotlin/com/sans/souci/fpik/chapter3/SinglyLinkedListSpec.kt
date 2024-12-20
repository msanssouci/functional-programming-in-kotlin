package com.sans.souci.fpik.chapter3

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs

class SinglyLinkedListSpec : ExpectSpec({
    expect("Init should not sof on a big list") {
        shouldNotThrow<StackOverflowError> { List.fill(50_000, 1) }
    }

    expect("Init with producer should not sof on a big list") {
        shouldNotThrow<StackOverflowError> {
            List.fillProducer(50_000, 1) { it + 1 }.should {
                println(List.head(it))
            }
        }
    }

    context("head") {
        expect("should return head for Cons") {
            val list = List.of(1, 2, 3)
            val result = List.head(list)
            result shouldBe 1
        }

        expect("should return null Nil") {
            val list = List.of<Int>()
            val result = List.head(list)
            result.shouldBeNull()
        }
    }

    context("tail") {
        expect("should return Nil for Nil") {
            val list = Nil
            val result = List.tail(list)
            result.shouldBeSameInstanceAs(Nil)
        }
        expect("should return tail for Cons") {
            val list = List.of(1, 2, 3)
            val result = List.tail(list)
            result shouldBe List.of(2, 3)
        }
    }

    context("tailNil") {
        expect("should return Nil for Nil") {
            val list = Nil
            val result = List.tailNil(list)
            result.shouldBeSameInstanceAs(Nil)
        }
        expect("should return tail for Cons") {
            val list = List.of(1, 2, 3)
            val result = List.tailNil(list)
            result shouldBe List.of(2, 3)
        }
    }

    context("tailEmpty") {
        expect("should return Nil for Nil") {
            val list = Nil
            val result = List.tailEmpty(list)
            result.shouldBeSameInstanceAs(Nil)
        }
        expect("should return tail for Cons") {
            val list = List.of(1, 2, 3)
            val result = List.tailEmpty(list)
            result shouldBe List.of(2, 3)
        }
    }

    context("fill") {
        expect("fill should return nil for n == 0") {
            List.fill(0, 1).shouldBeSameInstanceAs(Nil)
        }

        expect("n == 1 returns list with head and Nil tail") {
            val list = List.fill(1, 2)
            List.head(list) shouldBe 2
        }
    }

    context("setHead") {
        expect("Can set the head of a Nil") {
            val nilList = Nil
            List.setHead(nilList, 1).shouldBeInstanceOf<Cons<Int>>().should {
                it.head shouldBe 1
            }
        }

        expect("Can set the head of a Cons") {
            val consList = List.of(1, 2, 3)
            List.setHead(consList, 4).shouldBeInstanceOf<Cons<Int>>().should {
                it.head shouldBe 4
                it.tail shouldBeSameInstanceAs List.tail(consList)
            }
        }
    }

    context("drop") {
        expect("Can drop elements from Nil") {
            List.drop(Nil, 1).shouldBeSameInstanceAs(Nil)
        }

        expect("Can drop 0 elements from Cons returns same Cons") {
            val cons = List.of(1, 2, 3)
            List.drop(cons, 0).shouldBeSameInstanceAs(cons)
        }

        expect("Can drop -1 elements from Cons returns same Cons") {
            val cons = List.of(1, 2, 3)
            List.drop(cons, -1).shouldBeSameInstanceAs(cons)
        }

        expect("Can drop 1 elements from Cons") {
            val cons = List.of(1, 2, 3)
            List.drop(cons, 1).shouldBeInstanceOf<Cons<Int>>().shouldBeSameInstanceAs(List.tail(cons))
        }

        expect("Can drop 2 elements from Cons") {
            val cons = List.of(1, 2, 3)
            List.drop(cons, 2).shouldBeSameInstanceAs(List.tail(List.tail(cons)))
        }

        expect("Can drop 3 elements from Cons") {
            val cons = List.of(1, 2, 3)
            List.drop(cons, 3).shouldBeSameInstanceAs(Nil)
        }
    }

    context("dropWhile") {
        expect("Can drop while on Nil returns Nil") {
            List.dropWhile(Nil, { _ -> false }).shouldBeSameInstanceAs(Nil)
        }

        expect("Can drop while on Cons returns Nil if all are true") {
            val cons = List.of(1, 2, 3)
            List.dropWhile(cons, { _ -> true }) shouldBe Nil
        }

        val cons = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        expect("Can drop cons until a number 5 or greater is encountered") {
            List.dropWhile(cons, { a -> a < 5 }) shouldBe List.of(5, 6, 7, 8, 9, 10)
        }

        expect("Can drop odds on Cons returns Cons of only odds") {
            List.dropWhile(cons, { a -> a < 10 }) shouldBe List.of(10)
        }
    }

    context("Init") {
        expect("Init should return Nil if provided list is Nil") {
            List.init(Nil).shouldBeSameInstanceAs(Nil)
        }

        expect("Init should return Nil if provided list of size 1 is provided") {
            List.init(List.of(1)).shouldBeSameInstanceAs(Nil)
        }

        expect("Init should return only the first element if provided list of size 2 is provided") {
            List.init(List.of(1, 2)) shouldBe List.of(1)
        }

        expect("Init should drop the last element of a list") {
            val cons = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            List.init(cons) shouldBe List.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
        }
    }

    context("Sum") {
        expect("Sum should return 0 for Nil") {
            List.sum(Nil) shouldBe 0
        }

        expect("Sum should return 1 for Cons with 1 element") {
            List.sum(List.of(1)) shouldBe 1
        }

        expect("Sum should return 15 for Cons with 1 to 5") {
            List.sum(List.of(1, 2, 3, 4, 5)) shouldBe 15
        }
    }

    context("Product") {
        expect("Product should return 1 for Nil") {
            List.product(Nil) shouldBe 1
        }

        expect("Product should return 1 for Cons with 1 element") {
            List.product(List.of(1.0)) shouldBe 1
        }

        expect("Product should return 120 for Cons with 1 to 5") {
            List.product(List.of(1.0, 2.0, 3.0, 4.0, 5.0)) shouldBe 120.0
        }

        expect("Product should throw IllegalArgumentException for Cons with 1 to 5 containing 0.0") {
            shouldThrow<IllegalArgumentException> {
                List.product(List.of(1.0, 2.0, 3.0, 0.0, 4.0, 5.0))
            }
        }
    }
})
