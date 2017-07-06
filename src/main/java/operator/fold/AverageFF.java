package operator.fold;

import model.SensorData;
import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.api.java.tuple.Tuple5;

/**
 * Created by marco on 04/07/17.
 */
public class AverageFF implements FoldFunction<SensorData, Tuple5<SensorData, Long,Double,Long,Long>> {

    private boolean compute;

    public AverageFF(boolean compute){
        this.compute = compute;
    }

    @Override
    public Tuple5<SensorData, Long, Double, Long,Long> fold(Tuple5<SensorData, Long, Double, Long,Long> in, SensorData sensorData) throws Exception {
        if(in.f0 != null) {
            sensorData.setV(in.f0.getV() + (sensorData.getV() - in.f0.getV()) / (in.f1 + 1));
            Double currentTotalDistance = Double.valueOf(0);
            if (compute)
                currentTotalDistance = in.f2 + sensorData.computeDistance(sensorData.getX()-in.f0.getX(),sensorData.getY()-in.f0.getY());
            long start_timestamp = 0;
            long stop_timestamp = 0;
            if (in.f3 >= sensorData.getTs())
                start_timestamp = sensorData.getTs();
            else
                start_timestamp = in.f3;
            if (in.f4 > sensorData.getTs())
                stop_timestamp = in.f4;
            else
                stop_timestamp = sensorData.getTs();
            return new Tuple5<>(sensorData, in.f1 + 1,currentTotalDistance,start_timestamp,stop_timestamp);
        }
        else
            return new Tuple5<>(sensorData, (long)1,new Double(0),sensorData.getTs(),sensorData.getTs());

    }


}
