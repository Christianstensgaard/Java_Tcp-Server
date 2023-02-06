package Shared.Communication;

import java.nio.ByteBuffer;
import Shared.Communication.CommunicationTable.ActionType;
import Shared.Communication.CommunicationTable.TaskType;

public class DataPackage {
    private ActionType action;
    private TaskType task;
    private byte[] data;

    
    public ActionType getAction() {
        return action;
    }
    public TaskType getTask() {
        return task;
    }
    public byte[] getData() {
        return data;
    }

    public DataPackage(){}
    public DataPackage(ActionType action, TaskType task, byte[] data) {
        this.action = action;
        this.task = task;
        this.data = data;
    }

    public byte[] pack(){
        ByteWriter writer = new ByteWriter(1024);

        writer.writeInt(action.ordinal());
        writer.writeInt(task.ordinal());
        writer.writeBytes(data);
        return writer.getBytes();
    }

    public DataPackage unPack(byte[] data, int length){
        ByteReader reader = new ByteReader(data, length);

        action = ActionType.values()[reader.getNextInt()];
        task = TaskType.values()[reader.getNextInt()];
        this.data = reader.getBytes();
        return this;
    }

    private class ByteReader{
        public ByteReader(byte[] data, int dataLength){
            this.data = data;
            length = dataLength;
        }

        private byte[] data;
        private int length;
        private int current;

        public int getNextInt(){
            int value = ByteBuffer.wrap(data,current,4).getInt();
            current += 4;
            return value;
        }

        public byte[] getBytes(){
            int l = length-current;
            byte[] bytes = new byte[l];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = data[current++];
            }
            return bytes;
        }
    }

    private class ByteWriter{
        private byte[] data;
        private int current;

        public ByteWriter(int bufferSize){
            data = new byte[bufferSize];
            current = 0;
        }

        public void writeInt(int value){
            byte[] bytes = ByteBuffer.allocate(4).putInt(value).array();
            add(bytes);
        }

        public void writeBytes(byte[] data){
            add(data);
        }

        public byte[] getBytes(){
            byte[] buffer = new byte[current];
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = data[i];
            }
            return buffer;
        }

        private void add(byte[] data){
            for (int i = 0; i < data.length; i++) {
                this.data[current++] = data[i];
            }
        }
    }
}
