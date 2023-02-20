import { createTheme, ThemeProvider } from '@mui/material/styles';
import Button from '@mui/material/Button';
import { interests } from '../common/interestsList';
import { MAIN_COLOR } from '../../style/palette';

const theme = createTheme({
  palette: {
    primary: {
      main: MAIN_COLOR
    },
    secondary: {
      main: '#F0F0F0'
    }
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          height: '2rem',
          padding: '0',
          margin: '0.5rem',
          borderRadius: '40%',
        }
      }
    }
  }
});

interface Props {
  current: string,
  change: React.Dispatch<React.SetStateAction<string>>
}

const SubjectsList = ({ current, change }: Props) => {
  return(
    <ThemeProvider theme={theme}>
      {interests.map((interest) => (
        <Button
          key={interest[1]}
          id={interest[1]}
          color={current === interest[1] ? 'primary' : 'secondary'}
          variant='contained'
          onClick={(e) => {change(e.currentTarget.id)}}
        >
          {interest[0]}
        </Button>
      ))}
    </ThemeProvider>
  );
};

export default SubjectsList;