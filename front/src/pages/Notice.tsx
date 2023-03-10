import { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import { Notification } from '../components/notification';
import { getNotice } from '../api/user';
import { Header } from '../components/common';

interface NotificationData {
  notificationId: number,
  type: string,
  receiverBody: string,
}

const Notice = () => {
  const [notifications, setNotifications] = useState<NotificationData[]>([]);

  useEffect(() => {
    getNotice().then(data => {
      console.log(data);
      setNotifications(data);});
  }, []);

  return(
    <Box sx={{ width: '100%' }}>
      <Header />
      {notifications.map((noti) => (
        <Notification key={noti.notificationId} type={noti.type} body={noti.receiverBody} />
      ))}
    </Box>
  );
};

export default Notice;