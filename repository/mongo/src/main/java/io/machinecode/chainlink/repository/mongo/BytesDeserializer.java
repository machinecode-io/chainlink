package io.machinecode.chainlink.repository.mongo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.machinecode.chainlink.marshalling.jdk.JdkMarshaller;
import io.machinecode.chainlink.spi.marshalling.Marshaller;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class BytesDeserializer extends JsonDeserializer<Serializable> {

    //TODO Work out if we can access the one in the repo
    final Marshaller marshaller = new JdkMarshaller();

    @Override
    public Serializable deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        try {
            return marshaller.unmarshall(parser.getBinaryValue());
        } catch (final ClassNotFoundException e) {
            throw new IOException(e);
        }
    }
}