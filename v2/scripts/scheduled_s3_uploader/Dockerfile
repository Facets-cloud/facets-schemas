FROM python:3.7-alpine

WORKDIR /app

COPY src/requirements.txt ./

RUN pip install --upgrade pip && \
    pip install --no-cache-dir -r requirements.txt

COPY src /app

ENV DUMP_DIR $DUMP_DIR

ENV S3_BUCKET k8s-file-dumps

ENV CLUSTER $CLUSTER

CMD ["python", "scheduled_s3_uploader.py"]