'use client';

import { Typography, Card, Space, Tag } from 'antd';
import { CheckCircleOutlined } from '@ant-design/icons';
import * as React from "react";

const { Title, Paragraph } = Typography;

export default function Test1Page() {
  return (
    <Card style={{ maxWidth: 800, margin: '2rem auto' }}>
      <Space orientation="vertical" size="middle" style={{ width: '100%' }}>
        <Title level={2}>
          <CheckCircleOutlined style={{ marginRight: 12, color: '#52c41a' }} />
          Тест 1: Простая страница
        </Title>

        <Paragraph>
          Эта страница работает без Skia. Проверяем маршрутизацию и загрузку.
        </Paragraph>

        <Paragraph>
          <Tag color="blue">Status</Tag> ✅ Всё работает
        </Paragraph>
      </Space>
    </Card>
  );
}
