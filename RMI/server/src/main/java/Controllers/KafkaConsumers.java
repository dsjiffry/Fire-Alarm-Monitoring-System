package Controllers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumers implements Runnable {

	private final String bootstrapServer = "127.0.0.1:9092";
	private final String grp_id = "desktop-kafka";
	private ArrayList<String> topics; // Array List Of All the topics
	private HashMap<String, String> values; // Name of sensor and reading
	private Properties properties;

	public KafkaConsumers() {
		properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, grp_id);
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		topics = new ArrayList<String>();
		values = new HashMap<>();

		Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();

	}

	public void addTopic(String sensorName) {
		topics.add(sensorName);
	}

	public void removeTopic(String sensorName) {
		topics.remove(sensorName);
	}

	public String getReading(String sensorName) {
		if (!values.containsKey(sensorName)) {
			return "";
		}

		String reading = values.get(sensorName);
		while (reading == null) {
			ThreadSleep(100);
			reading = values.get(sensorName);
		}
		if (reading.contains("timeStamp")) {
			return reading.split(",")[0].replaceAll("\\{", "").replaceAll("\"", "").split(":")[1];
		}
		return reading;
	}

	public void run() {
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
		while (true) {
			if (topics.isEmpty()) {
				ThreadSleep(1000);
				continue;
			}
			consumer.subscribe(topics);
			synchronized (consumer) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
				for (ConsumerRecord<String, String> record : records) {
					values.put(record.topic(), record.value());
				}
			}
		}
	}

	private void ThreadSleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
