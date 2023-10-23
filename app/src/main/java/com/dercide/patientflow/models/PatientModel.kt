package com.dercide.patientflow.models

class PatientModel(id:Int?, name:String, surnames:String, birthdate:String, phone:String, photourl:String?) {

    var id:Int?
    var name:String
    var surnames:String
    var birthdate:String
    var phone:String
    var photourl:String?

    init {
        this.id = id
        this.name = name
        this.surnames = surnames
        this.birthdate = birthdate
        this.phone = phone
        this.photourl = photourl
    }
}