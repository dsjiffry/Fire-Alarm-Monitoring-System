[![Java CI with Maven](https://github.com/dsjiffry/Fire-Alarm-Monitoring-System/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)](https://github.com/dsjiffry/Fire-Alarm-Monitoring-System/actions?query=workflow%3A%22Java+CI+with+Maven%22)
[![Node.js CI](https://github.com/dsjiffry/Fire-Alarm-Monitoring-System/workflows/Node.js%20CI/badge.svg?branch=master)](https://github.com/dsjiffry/Fire-Alarm-Monitoring-System/actions?query=workflow%3A%22Node.js+CI%22)

# Fire Alarm Monitoring System


## Instructions

 * The system should have a web client application where users can view the status of all fire alarm
sensors. For each sensor, the web application should display whether the fire alarm sensor is
active, the location (floor no, room no), and the smoke level (1-10) and the CO2 level (1-10
scale). If the smoke level or CO2 level is above 5, then they should be marked in red. You can
decide on the user interface as you wish. The sensor details should be updated every 40
seconds.
 * A desktop client application where users can view the same information from a desktop client.
The information is refreshed every 30 seconds.
 * The desktop client application should have an administrator login, where an administrator can
add/register new fire alarm sensors. To register, the floor number and the room no should be
given. The administrator can edit sensor details as well.
 * An alert can be displayed on the desktop client when the CO2 level or Smoke level is moves to a
value greater than 5, of any sensor. An email and an SMS can be sent in such an occasion.

## Architecture

<img src="https://github.com/dsjiffry/Fire-Alarm-Monitoring-System/raw/master/Architecture.png" width="70%">
