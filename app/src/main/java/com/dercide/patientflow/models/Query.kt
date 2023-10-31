package com.dercide.patientflow.models

class Query(idQueries:Int?, datetime:String, weight:Double, pressure:String, temperature:Double, currentsurgery:Boolean, selfmedication:String, diseasesandallergies:String, status:Int, prescription_idprescription:Int?, patients_idPatient:Int) {
    var idQueries:Int?
    var datetime:String
    var weight:Double
    var pressure:String
    var temperature:Double
    var currentsurgery:Boolean
    var selfmedication:String
    var diseasesandallergies:String
    var status:Int
    var prescription_idprescription:Int?
    var patients_idPatient:Int

    init {
        this.idQueries = idQueries
        this.datetime = datetime
        this.weight = weight
        this.pressure = pressure
        this.temperature = temperature
        this.currentsurgery = currentsurgery
        this.selfmedication = selfmedication
        this.diseasesandallergies = diseasesandallergies
        this.status = status
        this.prescription_idprescription = prescription_idprescription
        this.patients_idPatient = patients_idPatient
    }
}