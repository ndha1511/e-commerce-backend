
import { Tooltip } from 'react-bootstrap';
const CustomTooltip = (message: string, isValid?:string) => (props: any) => (
  <Tooltip id="button-tooltip" {...props}  className={`${isValid ? 'custom-tooltip1' : 'custom-tooltip'}`}>
      {message} {/* Hiển thị thông điệp */}
  </Tooltip>
);

export default CustomTooltip;
