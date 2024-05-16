package com.example.ripcurrent.tool.Check

fun ExtraSpaces(input:String):String{
    var output=""
    if(input==""){
        return input
    }
    if(input[input.length-1]==' '){
        for(i in 0..input.length-2){
            output+=input[i]
        }
    }else{
        output=input
    }
    return output
}