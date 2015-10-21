# Garage Application

This is a very simple implemenation of AKKA meant to represent a parking lot reservation system.

## Installation

use sbt to build the project.  Then 'test' at the prompt to execute the tests.
Interesting classes at this point are the LevelActor and SpotActor and their respective tests.

## Assumptions / Basic Nature of the Application

1. A "parkingSpace" is defined as a numbered space on a specific floor.  It might eventually need to include coordinates for proper plotting on a display, but we'll do that in a later iteration and assume the display knows where to plot the spots.
2. There can be x number of floors, and x number of "parkingSpace" on a floor.  These values can be configured in the control panel.
2. The application will be split into a series of user interfaces and a backend REST based api.  This app currently focuses just on the business logic in the API.
3. When the API is fully fleshed out, these calls will be available:
    * listSpots (provides details on available spots)
    * reserveSpot
    * takeSpot
    * releaseSpot

## Final comments

I had hoped to complete a MicroService using the new Akka HTTP library.  But I feel like at 2 days,
it's time I get something back to you.  I've invested much more time in this than I had hoped. It
took considerable time to re-orient myself on the Akka world view, and the various oddities that
can bite you.

Thank you for taking the time to talk to me on Monday.  I would delighted to receive any feedback
you might have on the work I've done.

Dan
