package com.example.thematicworks.model

import java.io.Serializable
import java.util.Date

class Appeal:Serializable {


    lateinit var id: String
    var ticketnumber:String=""
    var ticketname:String=""
    var phone:String=""
    var imagePath: String? = null
  var regulations:String=""
    var createTime = Date()
    var remark: String = ""//h 申訴理由2 a

}
