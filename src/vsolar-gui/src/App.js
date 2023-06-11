import {BrowserRouter as Router, Routes, Route, Link} from 'react-router-dom'
import './styles/global.css'
import {Login} from './pages/Login'
import {Root} from './pages/Root'
import {Setup} from './pages/Setup'
import {Ui} from './pages/Ui'
import {Env} from './pages/Env'


function App() {
  return (
   <div>
    <Router>
      <Routes>
        <Route path="/" element={<Ui />} />
        <Route path="/setup" element={<Ui />} />
        <Route path="/login" element={<Ui />} />
        <Route path="/ui" element={<Ui />} />
        <Route path="/ui/vsolar" element={<Ui />} />
        <Route path="*" element={<h1>Wrong Direction</h1>}></Route>
      </Routes>
    </Router>
   </div>
  );
}

export default App;
