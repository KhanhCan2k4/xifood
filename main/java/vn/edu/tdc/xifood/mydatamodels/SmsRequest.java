package vn.edu.tdc.xifood.mydatamodels;

public class SmsRequest {
    private Message[] messages;

    public SmsRequest(Message[] messages) {
        this.messages = messages;
    }

    public static class Message {
        private String from;
        private String text;
        private Destination[] destinations;

        public Message(String from, String text, Destination[] destinations) {
            this.from = from;
            this.text = text;
            this.destinations = destinations;
        }

        public static class Destination {
            private String to;

            public Destination(String to) {
                this.to = to;
            }
        }
    }
}