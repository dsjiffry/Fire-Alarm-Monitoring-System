package Controllers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

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
	private AtomicBoolean hasTopicsBeenUpdated;

	public KafkaConsumers() {
		properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, grp_id);
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		topics = new ArrayList<String>();
		values = new HashMap<>();
		hasTopicsBeenUpdated = new AtomicBoolean(true);
	}

	public void addTopic(String sensorName) {
		topics.add(sensorName);
		hasTopicsBeenUpdated.set(true);
	}

	public void removeTopic(String sensorName) {
		topics.remove(sensorName);
		hasTopicsBeenUpdated.set(true);
	}

	public HashMap<String, String> getReading(HashMap<String, String> sensorName) {
		for (Map.Entry<String, String> entry : sensorName.entrySet()) {
			if (!topics.contains(entry.getKey())) {
				topics.add(entry.getKey());
				hasTopicsBeenUpdated.set(true);
			}
			if (values.containsKey(entry.getKey())) {
				String reading = values.get(entry.getKey());
				if (reading.contains("timeStamp")) {
					reading = reading.split(",")[0].replaceAll("\\{", "").replaceAll("\"", "").split(":")[1];
				}
				sensorName.put(entry.getKey(), reading);
			}
		}
		return sensorName;
	}

	public void run() {
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
		while (true) {
			if (topics.isEmpty()) {
				ThreadSleep(1000);
				continue;
			}
			if (hasTopicsBeenUpdated.get()) {
				consumer.subscribe(topics);
				hasTopicsBeenUpdated.set(false);
			}
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
			for (ConsumerRecord<String, String> record : records) {
				values.put(record.topic(), record.value());
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
