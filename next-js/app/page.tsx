'use client';

import { Typography, Card, Row, Col, Button } from 'antd';
import { ExperimentOutlined } from '@ant-design/icons';
import Link from 'next/link';

const { Title, Paragraph } = Typography;

const tests = [
  { id: 0, title: 'Skia Canvas', description: 'Рисование через Skia', path: '/tests/test-0' },
  { id: 1, title: 'Simple Text', description: 'Просто текст, проверка layout', path: '/tests/test-1' },
];

export default function HomePage() {
  return (
    <div style={{ maxWidth: 1200, margin: '0 auto', padding: '2rem' }}>
      <Title level={1}>🎨 Песочница</Title>
      <Paragraph>Выбери тест для запуска:</Paragraph>

      <Row gutter={[24, 24]} style={{ marginTop: 32 }}>
        {tests.map((test) => (
          <Col xs={24} sm={12} lg={8} key={test.id}>
            <Card
              hoverable
              actions={[
                <Link key="link" href={test.path}>
                  <Button type="primary" icon={<ExperimentOutlined />}>
                    Запустить
                  </Button>
                </Link>
              ]}
            >
              <Card.Meta
                title={`Тест ${test.id}: ${test.title}`}
                description={test.description}
              />
            </Card>
          </Col>
        ))}
      </Row>
    </div>
  );
}
