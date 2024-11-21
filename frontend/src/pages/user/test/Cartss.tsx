import { forwardRef, useEffect } from "react";

const Cartss = forwardRef<HTMLDivElement>((_, ref) => {
  return (
    <div ref={ref as React.MutableRefObject<HTMLDivElement>} className="border" style={{ padding: "10px", height: 60, display: "inline-block" }}>
      <i className="bi bi-cart " id="tets1" style={{ fontSize: 24, color: "black" }}></i>
    </div>
  );
});

export default Cartss;
