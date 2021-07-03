package edu.vanderbilt.cs.streams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import edu.vanderbilt.cs.streams.BikeRide.DataFrame;
import edu.vanderbilt.cs.streams.BikeRide.LatLng;

public class BikeStats {

    private BikeRide ride;

    public BikeStats(BikeRide ride) {
        this.ride = ride;
    }

    /**
     * @ToDo:
     *
     * Create a stream of DataFrames representing the average of the
     * sliding windows generated from the given window size.
     *
     * For example, if a windowSize of 3 was provided, the BikeRide.DataFrames
     * would be fetched with the BikeRide.fusedFramesStream() method. These
     * frames would be divided into sliding windows of size 3 using the
     * StreamUtils.slidingWindow() method. Each sliding window would be a
     * list of 3 DataFrame objects. You would produce a new DataFrame for
     * each window by averaging the grade, altitude, velocity, and heart
     * rate for the 3 DataFrame objects.
     *
     * For each window, you should use the coordinate of the first DataFrame in the window
     * for the location.
     *
     * @param windowSize
     * @return
     */
    public Stream<BikeRide.DataFrame> averagedDataFrameStream(int windowSize){
    	List<DataFrame> result = new ArrayList();
    	Stream<List<DataFrame>> test = StreamUtils.slidingWindow(ride.fusedFramesStream().collect(Collectors.toList()), windowSize);
    	test.forEach( it->{
    		LatLng c = it.get(0).coordinate;
    		double velocity = StreamUtils.averageOfProperty((DataFrame d) -> d.velocity).apply(it);
    		double grade = StreamUtils.averageOfProperty((DataFrame d) -> d.grade).apply(it);;
    		double heartRate = StreamUtils.averageOfProperty((DataFrame d) -> d.heartRate).apply(it);;
    		double altitude = StreamUtils.averageOfProperty((DataFrame d) -> d.altitude).apply(it);;
    		DataFrame df = new DataFrame(c, grade, altitude, velocity, heartRate);
    		result.add(df);
    	});
        return result.stream();
    }

    // @ToDo:
    //
    // Determine the stream of unique locations that the
    // rider stopped. A location is unique if there are no
    // other stops at the same latitude / longitude.
    // The rider is stopped if velocity = 0.
    //
    // For the purposes of this assignment, you should use
    // LatLng.equals() to determine if two locations are
    // the same.
    //
    public Stream<LatLng> locationsOfStops() {
    	Set<LatLng> set = new HashSet();
    	Stream<DataFrame> test = ride.fusedFramesStream();
    	test.forEach( it->{
    		if(it.velocity == 0) {
    			set.add(it.coordinate);
    		}
    	});
        return set.stream();
    }

}
