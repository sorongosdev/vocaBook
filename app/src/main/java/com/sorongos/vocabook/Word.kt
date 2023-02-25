package com.sorongos.vocabook

/**data class : 데이터를 홀딩하기 위한 클래스
 * 자바는 없음
 * 상속 불가
 * */
data class Word(
    val text : String,
    val mean : String,
    val type : String
)
