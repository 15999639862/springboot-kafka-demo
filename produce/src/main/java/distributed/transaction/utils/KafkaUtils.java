package distributed.transaction.utils;

import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * @author Dean
 */
public class KafkaUtils {
	private final static Logger LOGGER = LoggerFactory.getLogger(KafkaUtils.class);

	private static Producer<String, String> producer;

	private static KafkaConsumer<String, String> consumer;

	static {
		Properties producerProps = new Properties();
		//必需的3个参数
		producerProps.put("bootstrap.servers", "localhost:9092");
		producerProps.put("key.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		producerProps.put("value.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		producer = new KafkaProducer<>(producerProps);

		Properties consumerProps = new Properties();
		consumerProps.put("bootstrap.servers", "localhost:9092");
		consumerProps.put("key.deserializer",
				"org.apache.kafka.common.serialization.StringDeserializer");
		consumerProps.put("value.deserializer",
				"org.apache.kafka.common.serialization.StringDeserializer");
		consumerProps.put("group.id", "VoucherGroup");
		//关闭自动提交offset
		consumerProps.put("enable.auto.commit", "false");
		consumer = new KafkaConsumer<>(consumerProps);
	}

	/**
	 * 同步发送消息
	 *
	 * @param topic topic
	 * @param value 消息内容
	 */
	public static void sendSync(String topic, String value) throws ExecutionException, InterruptedException {
		producer.send(new ProducerRecord<>(topic, value)).get();
	}
}
