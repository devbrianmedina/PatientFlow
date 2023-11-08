package com.dercide.patientflow.models

class Prescription(idPrescription:Int?, observations:String, medicines:String, datetime:String) {

    var idPrescription:Int?
    var observations:String
    var medicines:String
    var datetime:String

    init {
        this.idPrescription = idPrescription
        this.observations = observations
        this.medicines = medicines
        this.datetime = datetime
    }
}