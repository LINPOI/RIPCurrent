package com.example.ripcurrent.tool.Check

/*
    vararg為多組參數陣列

 */
fun NullData(vararg string: String):Boolean{
    string.forEach {
        if(it == ""){
            return true
        }
    }
    return false
}
fun NullData(string: String):Boolean{
    return string == ""
}
fun NullData(vararg int: Int):Boolean{
    int.forEach {
        if(it == 0 ){
            return true
        }
    }
    return false
}