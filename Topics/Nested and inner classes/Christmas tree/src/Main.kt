class ChristmasTree(var color: String) {

    // create function putTreeTopper()
    fun putTreeTopper(color: String) {
        val topper = TreeTopper(color)
        topper.sparkle()
    }

    inner class TreeTopper(var color: String) {

        // create function sparkle()
        fun sparkle() {
            println("The sparkling $color tree topper looks stunning on the ${this@ChristmasTree.color} Christmas tree!")
        }
    }
}