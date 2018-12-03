# Java Video Filtering Tool
This repo contains the java source code for a video capturing and filtering application. It was intended to be ran anywhere a JVM can be insalled. It was targeted to be run on a cloud container to process video from remote sources. It contains both a filtering engine and a filter tuning mechanism for on the fly changes to filter settings, It also contains utilities for communicating with an Amazon Web Services cloud account for file storage.

## Code Structure
1. Main:  Main entry point to application.
2. Util:  Various utility classes/functions used by the application.
3. Engine:  A driver that captures, processes, and saves video frames into video files for upload to a remote server.
4. Filter:  An image filtering architecture that processes raw video by passing frames through a set of cascaded filters.
3. GUI:  A simple UI to dynamically adjust filter settings to optimize filter output.

## Getting Started
### Dependencies
1. JavaCPP - Java wrappers of common c++ libraries.
2. OpenCV - Open source computer vision library.
2. JavaCV - Java wrappers of the opencv library binaries.


### Installing
1. Install opencv binaries.
2. Install JavaCPP wrappers.
3. Install JavaCV wrappers.
4. Clone this repository.
5. Install java IDE (Optional

### Running
To run, launch from the command line or create new project with favorite IDE.
