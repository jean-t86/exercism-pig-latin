object PigLatin {

    fun translate(phrase: String): String {
        val strBuilder = StringBuilder()

        val words = phrase.split(" ")
        for (i in words.indices) {
            val ruleOneWord = ruleOne(words[i])
            val ruleTwoWord = ruleTwo(words[i])
            val ruleThreeWord = ruleThree(words[i])
            val ruleFourWord = ruleFour(words[i])
            var newWord = ""

            when {
                ruleOneWord != words[i] -> newWord = ruleOneWord
                ruleFourWord != words[i] -> newWord = ruleFourWord
                ruleThreeWord != words[i] -> newWord = ruleThreeWord
                ruleTwoWord != words[i] -> newWord = ruleTwoWord
            }

            strBuilder.append(newWord)
            if (i != words.size - 1) {
                strBuilder.append(" ")
            }
        }

        return strBuilder.toString()
    }

    // Rule 1: If a word begins with a vowel sound, add an "ay" sound to the end of the word. Please note that "xr"
    // and "yt" at the beginning of a word make vowel sounds (e.g. "xray" -> "xrayay", "yttria" -> "yttriaay").
    private val voyels = mutableSetOf('a', 'e', 'i', 'o', 'u')

    fun ruleOne(word: String): String {
        val strBuilder = StringBuilder(word)

        val firstTwoChars = word.substring(0..1)
        if (firstTwoChars == "xr" ||
            firstTwoChars == "yt" ||
            voyels.contains(word[0])
        ) {
            strBuilder.append("ay")
        }

        return strBuilder.toString()
    }

    // Rule 2: If a word begins with a consonant sound, move it to the end of the word and then add an "ay" sound to
    // the end of the word. Consonant sounds can be made up of multiple consonants, a.k.a. a consonant cluster
    // (e.g. "chair" -> "airchay").
    fun ruleTwo(word: String): String {
        val strBuilder = StringBuilder(word)

        getConsonantCluster(word)?.let { consonantSound ->
            if (consonantSound != "xr" && consonantSound != "yt") {
                strBuilder.clear()
                strBuilder.append(word.substringAfter(consonantSound))
                strBuilder.append(consonantSound)
                strBuilder.append("ay")
            }
        }

        return strBuilder.toString()
    }

    private fun getConsonantCluster(word: String): String? {
        val consonant = word.takeWhile { !voyels.contains(it) }
        return if (consonant.isNotEmpty()) consonant else null
    }

    // Rule 3: If a word starts with a consonant sound followed by "qu", move it to the end of the word, and then add
    // an "ay" sound to the end of the word (e.g. "square" -> "aresquay").
    fun ruleThree(word: String): String {
        val strBuilder = StringBuilder(word)

        getConsonantCluster(word)?.let { consonantSound ->
            val afterConsonantSound = word.substringAfter(consonantSound)
            if (consonantSound.last() == 'q' && afterConsonantSound.first() == 'u') {
                strBuilder.clear()
                strBuilder.append(afterConsonantSound.substringAfter('u'))
                strBuilder.append(consonantSound)
                strBuilder.append("uay")
            }
        }

        return strBuilder.toString()
    }

    // Rule 4: If a word contains a "y" after a consonant cluster or as the second letter in a two letter word it makes
    // a vowel sound (e.g. "rhythm" -> "ythmrhay", "my" -> "ymay").
    fun ruleFour(word: String): String {
        val strBuilder = StringBuilder(word)

        if (word.length == 2 && word[1] == 'y') {
            strBuilder.clear()
            strBuilder.append(word.reversed())
            strBuilder.append("ay")
        } else if (word.contains('y') && word.indexOf('y') != 0) {
            voyels.add('y')
            getConsonantCluster(word)?.let { consonantSound ->
                strBuilder.clear()
                val afterConsonantSound = word.substringAfter(consonantSound)
                strBuilder.append(afterConsonantSound)
                strBuilder.append(consonantSound)
                strBuilder.append("ay")
            }
            voyels.remove('y')
        }

        return strBuilder.toString()
    }
}
