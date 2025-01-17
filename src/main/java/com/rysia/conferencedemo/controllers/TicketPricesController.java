package com.rysia.conferencedemo.controllers;

import com.rysia.conferencedemo.dto.TicketPriceDTO;
import com.rysia.conferencedemo.models.TicketPrice;
import com.rysia.conferencedemo.repositories.TicketPriceRepository;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class TicketPricesController {
    @Autowired
    private TicketPriceRepository ticketPriceRepository;

    @Autowired
    private ModelMapper modelMapper;

    @ApiOperation(value = "LIST ALL TICKET PRICES")
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketPrice>> list() {
        List<TicketPrice> ticketPrices = this.ticketPriceRepository.findAll();
        return ticketPrices.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<List<TicketPrice>>(ticketPrices, HttpStatus.OK);
    }

    @ApiOperation(value = "GET A UNIQUE TICKET PRICE")
    @GetMapping("/ticket/{id}")
    public ResponseEntity<TicketPrice> get(@PathVariable(value = "id") Long id) {
        Optional<TicketPrice> optionalTicketPrice = this.ticketPriceRepository.findById(id);
        return !optionalTicketPrice.isPresent() ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<TicketPrice>(optionalTicketPrice.get(), HttpStatus.OK);
    }

    @ApiOperation(value = "CREATE A TICKET")
    @PostMapping("/ticket")
    public ResponseEntity<TicketPrice> create(@RequestBody @Valid final TicketPriceDTO ticketPriceDTO) {
        return new ResponseEntity<TicketPrice>(this.ticketPriceRepository.saveAndFlush(convertToEntity(ticketPriceDTO)),
                HttpStatus.CREATED);
    }

    @ApiOperation(value = "UPDATE TICKET")
    @RequestMapping(value = "/ticket/{id}", method = RequestMethod.PUT)
    public ResponseEntity<TicketPrice> update(@PathVariable(value = "id") Long id, @RequestBody @Valid TicketPriceDTO ticketPriceDTO) {
        Optional<TicketPrice> optionalTicketPrice = this.ticketPriceRepository.findById(id);
        boolean existsTicket = optionalTicketPrice.isPresent();
        TicketPrice existingTicketPrice = new TicketPrice();

        if (existsTicket) {
            existingTicketPrice = optionalTicketPrice.get();
            BeanUtils.copyProperties(convertToEntity(ticketPriceDTO), existingTicketPrice, "ticket_price_id");
        }
        return existsTicket ? new ResponseEntity<TicketPrice>(this.ticketPriceRepository.saveAndFlush(existingTicketPrice), HttpStatus.ACCEPTED) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private TicketPrice convertToEntity(TicketPriceDTO ticketPriceDTO) {
        TicketPrice ticketPrice = modelMapper.map(ticketPriceDTO, TicketPrice.class);
        return ticketPrice;
    }

    @ApiOperation(value = "DELETE A TICKET")
    @RequestMapping(value = "/ticket/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        if (Optional.ofNullable(this.ticketPriceRepository.findById(id)).isPresent()) {
            this.ticketPriceRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
