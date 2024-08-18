import './App.css';
import React from 'react';

import JHipsterLiteNeonBlue from '@assets/JHipster-Lite-neon-blue.png';
import ReactLogo from '@assets/ReactLogo.png';
import { Button } from 'react-bootstrap';
import { Route, Routes } from 'react-router-dom';
import SifrarnikTable from '@/common/primary/app/components/sifrarniktable/sifrarnik-table';
import NewSifrarnik from '@/common/primary/app/components/newsifrarnik/new-sifrarnik';
import ValueList from '@/common/primary/app/components/valuelist/value-list';

function App() {
  return (
    <div className={"p-5"}>
      <Routes>
        <Route path={"/"} element={<SifrarnikTable/>}/>
        <Route path={"/edit-table/:tableName"} element={<ValueList/>} />
      </Routes>
    </div>

  );
}

export default App;
