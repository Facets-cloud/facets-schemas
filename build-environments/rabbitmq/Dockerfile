FROM rabbitmq:3.8.0-alpine

RUN apk add --update --no-cache \
    curl \
    unzip \
    wget 

#RUN curl https://dl.bintray.com/rabbitmq/community-plugins/3.7.x/rabbitmq_delayed_message_exchange/rabbitmq_delayed_message_exchange-20171201-3.7.x.zip > rabbitmq_delayed_message_exchange-20171201-3.7.x.zip && \
#unzip rabbitmq_delayed_message_exchange-20171201-3.7.x.zip && \
#rm -f rabbitmq_delayed_message_exchange-20171201-3.7.x.zip && \
#mv rabbitmq_delayed_message_exchange-20171201-3.7.x.ez plugins/

RUN wget https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/download/v3.8.0/rabbitmq_delayed_message_exchange-3.8.0.ez && \
    mv rabbitmq_delayed_message_exchange-3.8.0.ez plugins/
RUN rabbitmq-plugins enable rabbitmq_delayed_message_exchange
