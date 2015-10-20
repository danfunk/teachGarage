# Garage Application

This is an example of how to build a simple Microservice in scala using the new Akka-Http library. 

## Installation

use sbt to build the project.  Then 'run' at the sbt prompt will start the server on localhost:9000

## Assumptions / Basic Nature of the Application

1. The application will be split into a series of user interfaces and a backend REST based api.  I will focus on the API, but will include some calls to demonstrate the process.
1. There will be the following calls on the API
    * listSpots (provides details on available spots)
    * reserveSpot
    * takeSpot
    * releaseSpot
1. A spot is defined as a numbered space on a specific floor.  It might need to include coordinates for proper plotting on a display, but we'll do that in a later iteration and assume the display knows where to plot the spots.

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## License

