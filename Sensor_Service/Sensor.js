const mongoose = require("mongoose")

const schema = mongoose.Schema({
  username: String,
  sensorUID : String,
  floor: Number,
  room: Number,
  sensorType: String,
  status : String
})

module.exports = mongoose.model("Sensor", schema)