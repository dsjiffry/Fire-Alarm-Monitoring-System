[![Java CI with Maven](https://github.com/dsjiffry/Fire-Alarm-Monitoring-System/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)](https://github.com/dsjiffry/Fire-Alarm-Monitoring-System/actions?query=workflow%3A%22Java+CI+with+Maven%22)
[![Node.js CI](https://github.com/dsjiffry/Fire-Alarm-Monitoring-System/workflows/Node.js%20CI/badge.svg?branch=master)](https://github.com/dsjiffry/Fire-Alarm-Monitoring-System/actions?query=workflow%3A%22Node.js+CI%22)

# Fire Alarm Monitoring System

## Members and Details
 * IT18050318 - M.A. Zeid    - 076 461 9998 - it18050318@my.sliit.lk
 * IT17029896 - D.S. Jiffry  - 076 643 0330 - dsjiffry@gmail.com
 * IT18060690 - M.R.M. Rifan - 071 647 4716 - Mohamedrifan@live.com
 * IT18200034 - M.A.F. Hasna - 077 860 5567 - hanaanees95@gmail.com

## Instructions

 * The system should have a web client application where users can view the status of all fire alarm
sensors. For each sensor, the web application should display whether the fire alarm sensor is
active, the location (floor no, room no), and the smoke level (1-10) and the CO2 level (1-10
scale). If the smoke level or CO2 level is above 5, then they should be marked in red. You can
decide on the user interface as you wish. The sensor details should be updated every 15
seconds.
 * A desktop client application where users can view the same information from a desktop client.
The information is refreshed every 10 seconds.
 * The desktop client application should have an administrator login, where an administrator can
add/register new fire alarm sensors. To register, the floor number and the room no should be
given. The administrator can edit sensor details as well.
 * An alert can be displayed on the desktop client when the CO2 level or Smoke level is moves to a
value greater than 5, of any sensor. An email and an SMS can be sent in such an occasion.

## Architecture

<img src="https://github.com/dsjiffry/Fire-Alarm-Monitoring-System/raw/master/Architecture.png" width="70%">

## Strategy

  * In our system we have identified the Alert Service and the sensor API as the critical services. In case one of the other services was down when a fire started we wanted to make sure that the alert would still go out to the users. To do this we implemented a local caching mechanism. When the alert service first comes online it would get the emails and phone numbers of the users from the Authentication service and store them locally.
  * In case a fire occurred the sensor API would call on the Alert Service and then the alert service would fetch the emails and numbers from the Authentication service. Now in case the Authentication service is down when the Alert service needs the details. It would send out the emails and messages to the addresses and numbers it has cached. This way we can guarantee that even if all the users don’t get notified at least some of them will be. Since in this kind of safety system it is better to at least notify some people than none at all.
  * This mechanism prevents our Alert service from being completely dependent on the Authentication service. When requesting emails and numbers of the Authentication service is reachable then the cache would also be updated.


