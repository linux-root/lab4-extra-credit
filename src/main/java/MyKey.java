import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class MyKey implements WritableComparable<MyKey> {
	private String stationId;
	private int temperature;

	@Override
	public String toString() {
		return stationId + "\t" + temperature;
	}

	public MyKey() {
	}

	public MyKey(String stid, int temp) {
		this.stationId = stid;
		this.temperature = temp;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(stationId);
		out.writeInt(temperature);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		String stid = in.readUTF();
		int temp = in.readInt();
		this.stationId = stid;
		this.temperature = temp;
	}

	@Override
	public int compareTo(MyKey o) {
		int x = this.stationId.compareTo(o.stationId);
		if (x == 0) {
			return o.temperature - this.temperature;
		} else {
			return x;
		}
	}

}
