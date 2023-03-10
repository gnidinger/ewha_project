import styled from 'styled-components';
import { MAIN_COLOR } from './palette';

export const Input = styled.input`
  width: 100%;
  height: 2.4rem;
  padding-left: 0.6rem;
  outline: none;
  border: none;
  border-bottom: 0.14rem solid #5F5F5F;

  :focus {
    border-bottom: 0.16rem solid ${MAIN_COLOR};
  }
`;