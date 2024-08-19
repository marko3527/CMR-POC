import React, { useEffect, useState } from 'react';
import { Button, Col, Form, InputGroup, Row } from 'react-bootstrap';
import {
  ColumnDefinitionModel,
  NewSifrarnikColumnType,
  TableDefinitionModel,
} from '@/common/primary/app/components/newsifrarnik/table-definition-model';
import axios from 'axios';
import { fetchTypeOptions } from '@/common/primary/app/service/sifrarnik-service';
import { DropdownModel } from '@/common/primary/app/components/valuelist/value-list.model';
import Select from 'react-select';

export interface INewSifrarnikProps {
  sifrarnikModel: TableDefinitionModel,
  setSifrarnikModel: React.Dispatch<React.SetStateAction<TableDefinitionModel>>
  existingTables: TableDefinitionModel[]
}

const NewSifrarnik = (props: INewSifrarnikProps) => {
  const [typeOptions, setTypeOptions] = useState<string[]>([]);
  const [fkDisplayValueColumns, setFkDisplayValueColumns] = useState<string[]>([]);

  useEffect(() => {
    fetchTypeOptions().then(data => {
      console.log(data.map((option: DropdownModel) => option.value))
      setTypeOptions(data.map((option: DropdownModel) => option.value))
    })
  }, [])

  const nazivSifrarnikaOnChange = (value: string) => {
    props.setSifrarnikModel((prevState) => ({
      ...prevState,
      tableName: value
    }))
  }

  const kolonaOnChange = (index: number, key: string, value: any) => {
    console.log(value)
    props.setSifrarnikModel((prevState) => ({
      ...prevState,
      columns: prevState.columns.map((kolona, kolonaIndex) => {
        if (kolonaIndex === index) {
          (kolona as any)[`${key}`] = value
          return ({...kolona})
        } else return kolona
      })
    }))
  }

  const addKolonaToSifrarnik = () => {
    props.setSifrarnikModel((prevState) => ({
      ...prevState,
      columns: [...prevState.columns, initNewKolona()]
    }))
  }

  const removeKolona = (index:number) => {
    props.setSifrarnikModel((prevState) => ({
      ...prevState,
      columns: prevState.columns.filter((kolona, kolonaIndex) => kolonaIndex !== index)
    }))
  }

  const foreignKeyChecked = (index: number, event: any) => {
    if (!event.target.checked) {
      kolonaOnChange(index, "referencedTableName", undefined)
      kolonaOnChange(index, "multiselectColumn", undefined)
    } else {
      kolonaOnChange(index, "columnType", typeOptions.filter(typeOption => typeOption === NewSifrarnikColumnType.NUMBER)[0])
    }
    kolonaOnChange(index, "foreignKey", event.target.checked)
  }

  const columnTypeDisabled = (index: number) => {
    return props.sifrarnikModel.columns[index].foreignKey
  }

  const onReferencedTableNameChanged = (e: any, index: number) => {
    axios.get(`http://localhost:8080/api/options/${e.target.value}/columns`).then(res => setFkDisplayValueColumns(res.data))
    kolonaOnChange(index, "referencedTableName", e.target.value)
  }

  const initNewKolona = (): ColumnDefinitionModel => {
    return {
      defaultValue: '',
      columnName: '',
      mandatory: false,
      identity: false,
      columnType: null,
      unique: false
    }
  }

  return(
    <>
      <Form.Label>Naziv sifrarnika</Form.Label>
      <InputGroup className="mb-3">
        <Form.Control value={props.sifrarnikModel?.tableName} onChange={e => nazivSifrarnikaOnChange(e.target.value)}/>
      </InputGroup>
      {props.sifrarnikModel?.columns.map((kolona, index) => (
        <div key={index} className={"row"}>
          <Col md={11}>
            <Row>
              <Col md={6}>
                <Form.Label>Kolona</Form.Label>
                <InputGroup className="mb-3">
                  <Form.Select value={props.sifrarnikModel.columns[index].columnType ? props.sifrarnikModel.columns[index].columnType : undefined}
                               onChange={e => kolonaOnChange(index, "columnType", e.target.value)}
                               disabled={columnTypeDisabled(index)}>
                    {typeOptions && typeOptions.map((column: string, index: number) => (
                      <option key={index}>{column}</option>
                    ))}
                  </Form.Select>
                  <Form.Control value={props.sifrarnikModel.columns[index].columnName} onChange={e => kolonaOnChange(index, "columnName", e.target.value)}/>
                </InputGroup>
              </Col>
              <Col md={2}>
                <Form.Label>Def vrijednost</Form.Label>
                {props.sifrarnikModel.columns[index].columnType === NewSifrarnikColumnType.BOOLEAN &&
                  <InputGroup className="mb-3">
                    <Form.Check checked={props.sifrarnikModel.columns[index].defaultValue} onChange={e => kolonaOnChange(index, "defaultValue", e.target.checked)}/>
                  </InputGroup>
                }
                {props.sifrarnikModel.columns[index].columnType !== NewSifrarnikColumnType.BOOLEAN &&
                  <InputGroup className="mb-3">
                    <Form.Control value={props.sifrarnikModel.columns[index].defaultValue} onChange={e => kolonaOnChange(index, "defaultValue", e.target.value)}/>
                  </InputGroup>
                }
              </Col>
              <Col md={2}>
                <Form.Label>Obavezan</Form.Label>
                <InputGroup className="mb-3">
                  <Form.Check checked={props.sifrarnikModel.columns[index].mandatory} onChange={e => kolonaOnChange(index, "mandatory", e.target.checked)}/>
                </InputGroup>
              </Col>
              <Col md={2}>
                <Form.Label>Unique podatak</Form.Label>
                <InputGroup className="mb-3">
                  <Form.Check checked={props.sifrarnikModel.columns[index].unique} onChange={e => kolonaOnChange(index, "unique", e.target.checked)}/>
                </InputGroup>
              </Col>
              <Col md={2}>
                <Form.Label>Foreign key</Form.Label>
                <InputGroup className="mb-3">
                  <Form.Check checked={props.sifrarnikModel.columns[index].foreignKey} onChange={e => foreignKeyChecked(index, e)}/>
                </InputGroup>
              </Col>
              {props.sifrarnikModel.columns[index].foreignKey &&
                <>
                  <Col md={3}>
                    <Form.Label>Vezani sifrarnik</Form.Label>
                    <InputGroup className="mb-3">
                        <Form.Select value={props.sifrarnikModel.columns[index].referencedTableName} onChange={e => onReferencedTableNameChanged(e, index)}>
                        {props.existingTables && props.existingTables.map((table: TableDefinitionModel, index: number) => (
                          <option key={index}>{table.tableName}</option>
                        ))}
                      </Form.Select>
                    </InputGroup>
                  </Col>
                  <Col md={3}>
                    <Form.Label>Kolona za prikaz</Form.Label>
                    <InputGroup className="mb-3">
                      <Form.Select value={props.sifrarnikModel.columns[index].fkDisplayValueColumn} onChange={e => kolonaOnChange(index, "fkDisplayValueColumn", e.target.value)}>
                        {fkDisplayValueColumns && fkDisplayValueColumns.map((column: string, index: number) => (
                          <option key={index}>{column}</option>
                        ))}
                      </Form.Select>
                    </InputGroup>
                  </Col>
                </>
              }
            </Row>
          </Col>
          <Col md={1} className={"mt-md-4"}>
            <Button variant={"danger"} onClick={() => removeKolona(index)}>Obrisi</Button>
          </Col>
          <hr/>
        </div>
      ))}
      <Button className={"mt-md-3"} onClick={addKolonaToSifrarnik}>Dodaj kolonu</Button>
    </>
  )
}

export default NewSifrarnik
