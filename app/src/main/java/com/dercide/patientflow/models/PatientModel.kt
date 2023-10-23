package com.dercide.patientflow.models

class PatientModel(idPatient:Int?, name:String, surnames:String, birthdate:String, phone:String, photourl:String?) {

    var idPatient:Int?
    var name:String
    var surnames:String
    var birthdate:String
    var phone:String
    var photourl:String?

    init {
        this.idPatient = idPatient
        this.name = name
        this.surnames = surnames
        this.birthdate = birthdate
        this.phone = phone
        this.photourl = photourl
    }
}