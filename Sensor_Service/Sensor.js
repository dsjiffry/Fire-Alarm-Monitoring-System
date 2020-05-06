const mongoose = require("mongoose")

//Mongoose Schema Model for Sensors 
const schema = mongoose.Schema({
  username: String,
  sensorUID : String,
  floor: Number,
  room: Number,
  sensorType: String,
  status : String,
  reading: Number
})

module.exports = mongoose.model("Sensor", schema)