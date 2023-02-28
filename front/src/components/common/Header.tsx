import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import Badge from '@mui/material/Badge';
import NotificationsIcon from '@mui/icons-material/Notifications';
import SendIcon from '@mui/icons-material/Send';
import { getCookie } from '../../api/cookie';
import { isNewNotification, logout } from '../../api/user';

interface Props {
  children?: string
}

const Header = ({ children }: Props) => {
  const [drawerOpened, setDrawerOpened] = useState<boolean>(false);
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
  const [invisibleNI, setInvisibleNI] = useState<boolean>(true);
  const [invisibleSI, setInvisibleSI] = useState<boolean>(true);

  const navigation = useNavigate();

  const toggleDrawer = (open: boolean) =>
    (event: React.KeyboardEvent | React.MouseEvent) => {
      if (
        event.type === 'keydown' &&
        ((event as React.KeyboardEvent).key === 'Tab' ||
          (event as React.KeyboardEvent).key === 'Shift')
      ) {
        return;
      }
      setDrawerOpened(open);
    };

  const handleLogout = async() => {
    if(isLoggedIn) {
      await logout();
      setIsLoggedIn(!isLoggedIn);
    }
    navigation('/login');
  };

  const clickNotice = () => {
    setInvisibleNI(true);
    navigation('/notice');
  };

  useEffect(() => {
    if(getCookie('ari_login')) {
      setIsLoggedIn(true);
      isNewNotification().then(data => setInvisibleNI(!data));
    }
  }, []);

  const list = () => (
    <Box
      sx={{ width: 250 }}
      role="presentation"
      onClick={toggleDrawer(false)}
      onKeyDown={toggleDrawer(false)}
    >
      <List>
        <Link to='/'>
          <ListItem disablePadding>
            <ListItemButton>
              <ListItemText primary='홈' />
            </ListItemButton>
          </ListItem>
        </Link>
        <Link to='/ari'>
          <ListItem disablePadding>
            <ListItemButton>
              <ListItemText primary='게시판' />
            </ListItemButton>
          </ListItem>
        </Link>
        {isLoggedIn &&
          <Link to='/mypage'>
            <ListItem disablePadding>
              <ListItemButton>
                <ListItemText primary='마이페이지' />
              </ListItemButton>
            </ListItem>
          </Link>
        }
        <ListItem disablePadding>
          <ListItemButton onClick={handleLogout}>
            <ListItemText primary={isLoggedIn ? '로그아웃' : '로그인'} />
          </ListItemButton>
        </ListItem>
      </List>
    </Box>
  );

  return (
    <Box sx={{ flexGrow: 1, position: 'relative' }}>
      <AppBar position="static" color='inherit'>
        <Toolbar>
          <IconButton
            onClick={toggleDrawer(true)}
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            {children}
          </Typography>
          <Badge variant='dot' invisible={invisibleNI} onClick={clickNotice} color="primary" sx={{ mr: 1 }}>
            <NotificationsIcon sx={{ fontSize: 25 }} color='action' />
          </Badge>
          <Badge variant='dot' invisible={invisibleSI} color="primary">
            <SendIcon sx={{ fontSize: 25 }} color='action' />
          </Badge>
        </Toolbar>
      </AppBar>
      <Drawer anchor='left' open={drawerOpened} onClose={toggleDrawer(false)}>{list()}</Drawer>
    </Box>
  );
};

export default Header;