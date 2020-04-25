const mongoose = require("mongoose")

const schema = mongoose.Schema({
  username: String,
  sensorUID : String,
  floor: Number,
  room: Number,
  sensorType: String
})

module.exports = mongoose.model("Sensor", schema)