variable "vsphereUser" {
  type = string
}

variable "vspherePassword" {
  type = string
}

variable "vsphereIp" {
    type = string
}

variable "vsphereDatacenter" {
  type = string
}

variable "vsphereHost" {
 type = string 
}

variable "vmDatastore" {
  type = string 
}

variable "vmCluster" {
    type = string
}

variable "vmTemplate" {
  type = string
}

variable "vmNetwork" {
  type = string
}

variable "vmName" {
    type = string
}

variable "vmMemory" {
    type = number
}

variable "vmCpu" {
  type = number
}