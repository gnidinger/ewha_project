import { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import { Header } from '../components/common';
import { getMessages } from '../api/chat';

const Message = () => {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    getMessages().then(data => setMessages(data));
  }, []);

  return(
    <Box sx={{ width: '100%' }}>
      <Header>메시지</Header>
      {messages.map((message) => (
        <></>
      ))}
    </Box>
  );
};

export default Message;