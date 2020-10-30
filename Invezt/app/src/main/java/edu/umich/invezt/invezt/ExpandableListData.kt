package edu.umich.invezt.invezt

import java.util.*

// Data used to initial expandable list view in LearnActivity
internal object ExpandableListData {
    val data: HashMap<String, List<String>>
        get() {
            val expandableListDetail =
                HashMap<String, List<String>>()
            val supportLine: MutableList<String> = ArrayList()
            supportLine.add("Summary")
            supportLine.add("Links")
            val resistanceLine: MutableList<String> = ArrayList()
            resistanceLine.add("Summary")
            resistanceLine.add("Links")
            val bearFlag: MutableList<String> = ArrayList()
            bearFlag.add("Summary")
            bearFlag.add("Links")
            val bullFlag: MutableList<String> = ArrayList()
            bullFlag.add("Summary")
            bullFlag.add("Links")
            val cupFlag: MutableList<String> = ArrayList()
            cupFlag.add("Summary")
            cupFlag.add("Links")
            val channelFlag: MutableList<String> = ArrayList()
            channelFlag.add("Summary")
            channelFlag.add("Links")
            val elliotFlag: MutableList<String> = ArrayList()
            elliotFlag.add("Summary")
            elliotFlag.add("Links")

            expandableListDetail["SUPPORT LINES"] = supportLine
            expandableListDetail["RESISTANCE LINES"] = resistanceLine
            expandableListDetail["BEAR FLAG"] = bearFlag
            expandableListDetail["BULL FLAG"] = bullFlag
            expandableListDetail["CUP AND HANDLE"] = cupFlag
            expandableListDetail["CHANNEL"] = channelFlag
            expandableListDetail["ELLIOT WAVES"] = elliotFlag

            return expandableListDetail
        }
}