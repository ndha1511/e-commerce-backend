interface Props {
    activeTab: string;
    handleButtonClick: (name: string) => void;
    tabNames: string[];
}

function Tab({activeTab, handleButtonClick, tabNames}: Props) {
    
    return (
        <div className="btn-group mb-3" role="group">
           {tabNames.map((name, index) => (
             <button
                key={index}
                className={`text-medium btn-group-insert-product-seller ${activeTab === name? 'active' : ''}`}
                onClick={() => handleButtonClick(name)}
             >
                {name}
             </button>
           ))}
        </div>
    );
}

export default Tab;