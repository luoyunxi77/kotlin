# kotlin
# Basics

## val and var

Kotlin supports two types of variables: changeable and unchangeable. With `val`, you can assign a value once. If you try to assign something again, you get an error. With `var`, you can assign a value, then change the value later in the program.

```kotlin
var fish = 1
fish = 2
val aquarium = 1
aquarium = 2

⇒ error: val cannot be reassigned
```

## when

```kotlin
if (numberOfFish == 0) {
    println("Empty tank")
} else if (numberOfFish < 40) {
    println("Got fish!")
} else {
    println("That's a lot of fish!")
}
```

There's a nicer way to write that series of `if`/`else if`/`else` statements in Kotlin, using the `when` statement, which is like the `switch` statement in other languages.

```kotlin
val numberOfFish = 50
when (numberOfFish) {
    0  -> println("Empty tank")
    in 1..39 -> println("Got fish!")
    else -> println("That's a lot of fish!")
}

⇒ That's a lot of fish!
```

## nullability

Programming errors involving nulls have been the source of countless bugs. Kotlin seeks to reduce bugs by introducing non-nullable variables.

```kotlin
var fishFoodTreats: Int? = 6
fishFoodTreats = fishFoodTreats?.dec() ?: 0
fishFoodTreats = fishFoodTreats!!.dec()
```

## arrays, lists, and loops

### list

```kotlin
val school = listOf("mackerel", "trout", "halibut")
val myList = mutableListOf("tuna", "salmon", "shark")
```

### array

There is no mutable version of an `Array`

```kotlin
val mix = arrayOf("fish", 2)
```

### loops

```kotlin
val school = arrayOf("shark", "salmon", "minnow")
for (element in school) {
    print(element + " ")
}

⇒ shark salmon minnow

for ((index, element) in school.withIndex()) {
    println("Item at $index is $element\n")
}

⇒ Item at 0 is shark
Item at 1 is salmon
Item at 2 is minnow
```



# Functions

## default value

```kotlin
fun shouldChangeWater(day: String, temperature: Int = 22, dirty: Int = 20): Boolean {
    return when {
        temperature > 30 -> true
        dirty > 30 -> true
        day == "Sunday" ->  true
        else -> false
    }
}

val shouldChangeWater = shouldChangeWater(day, dirty = 24)
```

## compact functions

```kotlin
fun isTooHot(temperature: Int) = temperature > 30
fun isDirty(dirty: Int) = dirty > 30
fun isSunday(day: String) = day == "Sunday"

fun shouldChangeWater(day: String, temperature: Int = 22, dirty: Int = 20): Boolean {
    return when {
        isTooHot(temperature) -> true
        isDirty(dirty) -> true
        isSunday(day) -> true
        else  -> false
    }
}
```

## filter

```kotlin
val decorations = listOf ("rock", "pagoda", "plastic plant", "alligator", "flowerpot")
println( decorations.filter {it[0] == 'p'})

⇒ [pagoda, plastic plant]
```

## lambdas

In other languages, lambdas are called *anonymous functions*, *function literals*, or similar names.

The parameters (and their types, if needed) go on the left of what is called a function arrow `->`. The code to execute goes to the right of the function arrow.

```kotlin
var dirtyLevel = 20
val waterFilter = { dirty : Int -> dirty / 2}
println(waterFilter(dirtyLevel))

⇒ 10
```

### higher-order function

```kotlin
fun updateDirty(dirty: Int, operation: (Int) -> Int): Int {
   return operation(dirty)
}

val waterFilter: (Int) -> Int = { dirty -> dirty / 2 }
println(updateDirty(30, waterFilter))

⇒ 15
```

Kotlin prefers that any parameter that takes a function is the last parameter.

```kotlin
var dirtyLevel = 19;
dirtyLevel = updateDirty(dirtyLevel) { dirtyLevel -> dirtyLevel + 23}
println(dirtyLevel)

⇒ 42
```

# Classes and objects

## Class

### constructor

There is a class `Aquarium` with three properties.

```kotlin
class Aquarium {
    var width: Int = 20
    var height: Int = 40
    var length: Int = 100
}
```

The constructor of `Aquarium` .

```kotlin
class Aquarium(length: Int = 100, width: Int = 20, height: Int = 40) {
   var length: Int = length
   var width: Int = width
   var height: Int = height
}
```

The more compact Kotlin way is to define the properties directly with the constructor, using `var` or `val`, and Kotlin also creates the getters and setters automatically.

```kotlin
class Aquarium(var length: Int = 100, var width: Int = 20, var height: Int = 40) {

}
```

### secondary constructors

Kotlin coding style says each class should have only one constructor, using default values and named parameters. Before writing a secondary constructor, consider whether a [factory function](https://kotlinlang.org/docs/reference/coding-conventions.html#factory-functions) would work instead, to keep the class definition clean.

```kotlin
constructor(numberOfFish: Int) : this() {
    val tank = numberOfFish * 2000 * 1.1
	  height = (tank / (length * width)).toInt()
}
```

### property getter and setter

Sometimes the value for a property needs to be adjusted or calculated, you can add an explicit property getter.

```kotlin
val volume: Int
    get() = width * height * length / 1000
		set(value) {
        height = (value * 1000) / (width * length)
    }
```

### singleton

Kotlin lets you declare a class where you can only create one instance of it by using the keyword `object` instead of `class`.

```kotlin
object GoldColor : FishColor {
   override val color = "gold"
}
```

## Visibility modifiers

In Kotlin, classes, objects, interfaces, constructors, functions, properties, and their setters can have *visibility modifiers*:

- `public` means visible outside the class. Everything is public by default, including variables and methods of the class.
- `internal` means it will only be visible within that module. A [module](https://kotlinlang.org/docs/reference/visibility-modifiers.html#modules) is a set of Kotlin files compiled together, for example, a library or application.
- `private` means it will only be visible in that class (or source file if you are working with functions).
- `protected` is the same as `private`, but it will also be visible to any subclasses.

## Subclasses and inheritance

In Kotlin, by default, classes cannot be subclassed. Similarly, properties and member variables cannot be overridden by subclasses (though they can be accessed).

You must mark a class as `open` to allow it to be subclassed. Similarly, you must mark properties and member variables as `open`, in order to override them in the subclass. 

```kotlin
open class Aquarium (open var length: Int = 100, open var width: Int = 20, open var height: Int = 40) {
    open var volume: Int
        get() = width * height * length / 1000
        set(value) {
            height = (value * 1000) / (width * length)
        }
}
```

### subclass

```kotlin
class TowerTank (override var height: Int, var diameter: Int): Aquarium(height = height, width = diameter, length = diameter) {
    override var volume: Int
    	get() = (width/2 * length/2 * height / 1000 * PI).toInt()
    	set(value) {
        height = ((value * 1000 / PI) / (width/2 * length/2)).toInt()
    	}    
}
```

## Abstract classes and interfaces

### abstract class

Abstract classes are always open; you don't need to mark them with `open`. Properties and methods of an abstract class are non-abstract unless you explicitly mark them with the `abstract` keyword.

```kotlin
abstract class AquariumFish {
    abstract val color: String
}

class Shark: AquariumFish() {
    override val color = "gray"
}

class Plecostomus: AquariumFish() {
    override val color = "gold"
}
```

![](https://codelabs.developers.google.com/codelabs/kotlin-bootcamp-classes/img/f240a3110b8eaa91.png)

### interface

```kotlin
interface FishAction  {
    fun eat()
}

class Shark: AquariumFish(), FishAction {
    override val color = "gray"
    override fun eat() {
        println("hunt and eat fish")
    }
}

class Plecostomus: AquariumFish(), FishAction {
    override val color = "gold"
    override fun eat() {
        println("eat algae")
    }
}
```

![](https://codelabs.developers.google.com/codelabs/kotlin-bootcamp-classes/img/5a4eec9d1b74dc0a.png)

### abstract classes VS interfaces

- Neither an abstract class nor an interface can be instantiated on its own, which means you cannot create objects of those types directly.
- Abstract classes have constructors.
- Interfaces can't have any constructor logic or store any state.

Composition often leads to better [encapsulation](https://en.wikipedia.org/wiki/Encapsulation_(computer_programming)), lower [coupling](https://en.wikipedia.org/wiki/Coupling_(computer_programming)) (interdependence), cleaner interfaces, and more usable code. For these reasons, using composition with interfaces is the preferred design. On the other hand, inheritance from an abstract class tends to be a natural fit for some problems. So you should prefer composition, but when inheritance makes sense Kotlin lets you do that too!

### interface delegation

There is a class `Plecostomus` implementing interfaces for both `FishAction` and `FishColor`.

```kotlin
interface FishAction {
    fun eat()
}

interface FishColor {
    val color: String
}

class Plecostomus: FishAction, FishColor {
    override val color = "gold"
    override fun eat() {
        println("eat algae")
    }
}
```

Create an object for `GoldColor` to implement `FishColor`, a `PrintingFishAction` class that implements `FishAction`

```kotlin
object GoldColor : FishColor {
   override val color = "gold"
}

class PrintingFishAction(val food: String) : FishAction {
    override fun eat() {
        println(food)
    }
}
```

Replace them with delegations

```kotlin
class Plecostomus (fishColor: FishColor = GoldColor):
        FishAction by PrintingFishAction("eat algae"),
        FishColor by fishColor
```

## Data class

A data class is similar to a `struct` in some other languages, but **data class objects are objects**. Assigning a data class object to another variable copies the reference to that object, not the contents. To copy the contents to a new object, use the `copy()` method.

```kotlin
data class Decoration(val rocks: String) {
}
```

## Enum

You can get the ordinal value of an enum using the `ordinal` property, and its name using the `name` property.

```kotlin
enum class Direction(val degrees: Int) {
    NORTH(0), SOUTH(180), EAST(90), WEST(270)
}

fun main() {
    println(Direction.EAST.name)
    println(Direction.EAST.ordinal)
    println(Direction.EAST.degrees)
}

⇒ EAST
2
90
```

## sealed class

A *sealed class* is a class that can be subclassed, but only inside the file in which it's declared.

This makes sealed classes a safe way to represent a fixed number of types. For example, sealed classes are great for [returning success or error from a network API](https://articles.caster.io/android/handling-optional-errors-using-kotlin-sealed-classes/).

```kotlin
sealed class Seal
class SeaLion : Seal()
class Walrus : Seal()

fun matchSeal(seal: Seal): String {
   return when(seal) {
       is Walrus -> "walrus"
       is SeaLion -> "sea lion"
   }
}
```

# Extensions

## pairs and triples

Pairs and triples are premade data classes for 2 or 3 generic items. 

### pairs

You can create a pair by creating an expression connecting two values, such as two strings, with the keyword `to`, then using `.first` or `.second` to refer to each value.

```kotlin
val equipment = "fish net" to "catching fish"
println("${equipment.first} used for ${equipment.second}")

⇒ fish net used for catching fish
```

### triples

Create a triple and print it with `toString()`, then convert it to a list with `toList()`. You create a triple using `Triple()` with 3 values. Use `.first`, `.second` and `.third` to refer to each value.

```kotlin
val numbers = Triple(6, 9, 42)
println(numbers.toString())
println(numbers.toList())

⇒ (6, 9, 42)
[6, 9, 42]
```

### Destructure

```kotlin
val equipment = "fish net" to "catching fish"
val (tool, use) = equipment
println("$tool is used for $use")

⇒ fish net is used for catching fish
```

## hash maps

Hash maps are sort of like a list of pairs, where the first value acts as a key.

```kotlin
val cures = hashMapOf("white spots" to "Ich", "red sores" to "hole disease")

cures.get("white spots")
cures["scale loss"]

cures.getOrDefault("bloating", "sorry, I don't know")
cures.getOrElse("bloating") {"No cure for this"}
```

## constants

### const vs. val

The value for `const val` is determined at compile time, where as the value for `val` is determined during program execution, which means, `val` can be assigned by a function at run time.

In addition, `const val` only works at the top level, and in singleton classes declared with `object`, not with regular classes.

```kotlin
object Constants {
    const val CONSTANT2 = "object constant"
}
val foo = Constants.CONSTANT2
```

### companion object

Kotlin does not have a concept of class level constants.

```kotlin
class MyClass {
    companion object {
        const val CONSTANT3 = "constant in companion"
    }
}
```

The basic difference between companion objects and regular objects is:

- Companion objects are initialized from the static constructor of the containing class, that is, they are created when the object is created.
- Regular objects are initialized lazily on the first access to that object; that is, when they are first used.

## extensions

### extension function

```kotlin
fun String.hasSpaces(): Boolean {
    val found = this.find { it == ' ' }
    return found != null
}
println("Does it have spaces?".hasSpaces())

⇒ true
```

### extension property

```kotlin
class AquariumPlant(val color: String, private val size: Int)

val AquariumPlant.isGreen: Boolean
   get() = color == "green"
```

### nullable receivers

The class you extend is called the *receiver*, and it is possible to make that class nullable.

```kotlin
fun AquariumPlant?.pull() {
   this?.apply {
       println("removing $this")
   }
}

val plant: AquariumPlant? = null
plant.pull()
```

# Practice

Let's put the theory into practice, there is a kotlin project called `POS`, please open it using `IntelliJ IDEA`([you can download it here](https://www.jetbrains.com/idea/download/index.html#section=mac)), and finish the practice in `Hello.kt`. After you finish your practice, click the run button next to the main function to check your results.

## 需求描述

商店里进行购物结算时会使用收银机（POS）系统，这台收银机会在结算时根据客户的购物车（Cart）中的商品（Item）和商店正在进行的优惠活动（Promotion）进行结算和打印收据（Receipt）。

已知该商店正在对部分商品进行“买二赠一”的优惠活动。

我们需要实现一个名为`getReceipt`的函数，该函数能够将指定格式的数据作为输入，然后在控制台中输出收据的文本。

输入格式（样例）：

```kotlin
listOf(
    "ITEM000001",
    "ITEM000001",
    "ITEM000001",
    "ITEM000001",
    "ITEM000001",
    "ITEM000003-2",
    "ITEM000005",
    "ITEM000005",
    "ITEM000005"
)
```

其中对`ITEM000003-2`来说，"-"之前的是标准的条形码，"-"之后的是数量。
当我们购买需要称量的物品的时候，会由称量的机器生成此类标签（Tag），收银机负责识别生成收据。


清单内容（样例）：

```
***<没钱赚商店>收据***
名称：可口可乐，数量：3瓶，单价：3.00(元)，小计：6.00(元)
名称：羽毛球，数量：5个，单价：1.00(元)，小计：4.00(元)
名称：苹果，数量：2斤，单价：5.50(元)，小计：11.00(元)
----------------------
总计：21.00(元)
节省：4.00(元)
**********************
```

## 数据
```javascript
function loadAllItems() {
  return [
    {
      barcode: 'ITEM000000',
      name: '可口可乐',
      unit: '瓶',
      price: 3.00
    },
    {
      barcode: 'ITEM000001',
      name: '雪碧',
      unit: '瓶',
      price: 3.00
    },
    {
      barcode: 'ITEM000002',
      name: '苹果',
      unit: '斤',
      price: 5.50
    },
    {
      barcode: 'ITEM000003',
      name: '荔枝',
      unit: '斤',
      price: 15.00
    },
    {
      barcode: 'ITEM000004',
      name: '电池',
      unit: '个',
      price: 2.00
    },
    {
      barcode: 'ITEM000005',
      name: '方便面',
      unit: '袋',
      price: 4.50
    }
  ];
}

function loadPromotions() {
  return [
    {
      barcodes: [
        'ITEM000000',
        'ITEM000001',
        'ITEM000005'
      ]
    }
  ];
}
```

## 作业提示

1. 可使用`loadAllItems()`方法获取全部的商品，该方法返回结果为一个包含了商品对象的数组（样例）：

   ```
   [ item1, item2, item3, ..., itemN ]
   ```

2. 每一个商品对象的结构如下（样例）：

   ```javascript
   {
      barcode: 'ITEM000000',
      name: '可口可乐',
      unit: '瓶',
      price: 3.00
   }
   ```

3. 可使用`loadPromotions()`方法获取全部的促销信息，该方法返回结果为一个包含有促销信息对象的数组（样例）：

   ```javascript
   [
      {
        barcodes: [
          'ITEM000000',
          'ITEM000001'
        ]
      }
   ]
   ```

# Further Reading

- [Kotlin Bootcamp](https://codelabs.developers.google.com/kotlin-bootcamp/)
- [Kotlin Language Guide](https://kotlinlang.org/docs/reference/)
