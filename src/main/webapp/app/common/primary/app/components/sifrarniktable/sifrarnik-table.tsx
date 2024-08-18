import React, { useEffect, useState } from 'react';
import { Button, Col, Modal, ModalBody, ModalFooter, ModalHeader, Row, Table } from 'react-bootstrap';
import NewSifrarnik from '@/common/primary/app/components/newsifrarnik/new-sifrarnik';
import { TableDefinitionModel } from '@/common/primary/app/components/newsifrarnik/table-definition-model';
import axios from 'axios';
import { fetchTables, fetchTypeOptions } from '@/common/primary/app/service/sifrarnik-service';
import { useNavigate } from 'react-router-dom';

const SifrarnikTable = () => {

  const [dodajSifrarnikModalOpened, setDodajSifrarnikModalOpened] = useState(false);
  const [sifrarnikModel, setSifrarnikModel] = useState<TableDefinitionModel>({
    tableName: '',
    columns: []
  });
  const [datatables, setDatatables] = useState<TableDefinitionModel[]>([])
  let navigate = useNavigate();

  useEffect(() => {
    fetchTables().then(setDatatables)
  }, [])

  const spremiClicked = () => {
    axios.post('http://localhost:8080/api/database', sifrarnikModel).then(res => {
      setSifrarnikModel({
        tableName: '',
        columns: []
      })
      setDodajSifrarnikModalOpened(false)
    })
  }

  const tableRowClicked = (tableName: string) => {
    navigate(`/edit-table/${tableName}`)
  }

  const odustaniClicked = () => {
    setSifrarnikModel({
      tableName: '',
        columns: []
    })
    setDodajSifrarnikModalOpened(false)
  }

  return(
    <>
      <div className={"row"}>
        <div className={"col-md-6"}>
          <Button onClick={() => setDodajSifrarnikModalOpened(true)}>Dodaj sifrarnik</Button>
        </div>
      </div>
      <Row>
        <Col md={6}>
          <Table className={"mt-md-5"} striped bordered hover>
            <thead>
            <tr>
              <th>Naziv sifrarnika</th>
            </tr>
            </thead>
            <tbody>
            {datatables.map(datatable => (
              <tr onClick={() => tableRowClicked(datatable.tableName)}>
                <td>{datatable.tableName}</td>
              </tr>
            ))}
            </tbody>
          </Table>
        </Col>
      </Row>
      <Modal show={dodajSifrarnikModalOpened} size={"xl"}>
        <ModalHeader>Dodavanje novog sifrarnika</ModalHeader>
        <ModalBody>
          <NewSifrarnik sifrarnikModel={sifrarnikModel} setSifrarnikModel={setSifrarnikModel} existingTables={datatables}/>
        </ModalBody>
        <ModalFooter>
          <div className={"row"}>
            <Button className={"col-md-6"} variant={"secondary"} onClick={() => odustaniClicked()}>Odustani</Button>
            <Button className={"col-md-6"} variant={'success'} onClick={() => spremiClicked()}>Spremi</Button>
          </div>
        </ModalFooter>
      </Modal>
    </>
  )
}

export default SifrarnikTable
