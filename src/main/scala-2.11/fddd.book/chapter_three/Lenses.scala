package fddd.book.chapter_three

/**
 * @author yoav @since 5/18/16.
 */
object Lenses extends App{

  case class Lens[Object, Value](get: Object => Value, set: (Object, Value) => Object)

  def compose[Outer, Inner, Value](outer: Lens[Outer, Inner], inner: Lens[Inner, Value]) = {
    Lens[Outer, Value](
      get = outer.get andThen inner.get,
      set = (obj, value) => outer.set(obj, inner.set(outer.get(obj), value)))
  }

  case class Address(no: String, city: String)
  case class Customer(name: String, address: Address)

  val customer  = Customer("Jojo", Address("254", "TLV"))

  val addressNoLens = Lens[Address, String](a => a.no, (a,no)=> a.copy(no=no))
  val customerLens = Lens[Customer, Address](c => c.address, (c,a)=> c.copy(address=a))

  val composingLens = compose[Customer, Address, String](customerLens, addressNoLens)

  println(s"composingLens.get(customer): ${composingLens.get(customer)}")
  println(s"composingLens.set(customer, '998877'): ${composingLens.set(customer, "998877")}")
}
